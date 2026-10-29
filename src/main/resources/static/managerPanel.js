const labels = {
        pending: "待审核",
        approved: "已通过",
        rejected: "已拒绝",
      };

      let applications = [];

      const rows = document.getElementById("rows");

      // 后端统一返回 Result：code 为 200 时取 data，否则抛出 message
      async function request(url, options) {
        const response = await fetch(url, options);
        const result = await response.json();
        if (result.code === 401) {
          location.href = "/managerLogin.html";
          throw new Error("请先登录");
        }
        if (result.code !== 200) {
          throw new Error(result.message || "请求失败");
        }
        return result.data;
      }

      function formatTime(value) {
        if (!value) return "—";
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) return "—";
        const pad = (n) => String(n).padStart(2, "0");
        const hhmm = `${pad(date.getHours())}:${pad(date.getMinutes())}`;
        const now = new Date();
        const yesterday = new Date(
          now.getFullYear(),
          now.getMonth(),
          now.getDate() - 1,
        );
        if (date.toDateString() === now.toDateString()) return `今天 ${hhmm}`;
        if (date.toDateString() === yesterday.toDateString())
          return `昨天 ${hhmm}`;
        return `${pad(date.getMonth() + 1)}月${pad(date.getDate())}日`;
      }

      function toApplication(user, status) {
        return {
          name: user.username,
          reason: user.reason || "未填写",
          contact: user.email || "未填写",
          time: formatTime(user.createdAt),
          code: user.code || "",
          status: status,
        };
      }

      async function loadApplications() {
        try {
          const [pending, approved, rejected] = await Promise.all([
            request("/api/user/get/uncommit"),
            request("/api/user/get/commit"),
            request("/api/user/get/rejected"),
          ]);
          applications = [
            ...(pending || []).map((u) => toApplication(u, "pending")),
            ...(approved || []).map((u) => toApplication(u, "approved")),
            ...(rejected || []).map((u) => toApplication(u, "rejected")),
          ];
          render();
          return true;
        } catch (error) {
          console.error(error);
          showToast("加载失败：" + error.message);
          return false;
        }
      }

      // 用户名、邮箱、原因都来自注册表单，渲染前必须转义，避免 XSS
      function escapeHtml(value) {
        const map = {
          "&": "&amp;",
          "<": "&lt;",
          ">": "&gt;",
          '"': "&quot;",
          "'": "&#39;",
        };
        return String(value ?? "").replace(/[&<>"']/g, (c) => map[c]);
      }

      function render() {
        const q = document.getElementById("search").value.trim().toLowerCase();
        const f = document.getElementById("status").value;
        const list = applications.filter(
          (x) =>
            (f === "all" || x.status === f) &&
            (!q ||
              [x.name, x.contact, x.reason, x.code]
                .join(" ")
                .toLowerCase()
                .includes(q)),
        );

        rows.innerHTML = list
          .map((x) => {
            const initial = escapeHtml(
              (x.name || "").slice(0, 1).toUpperCase(),
            );
            const name = escapeHtml(x.name);
            const actions =
              x.status === "pending"
                ? `<button class="approve" data-action="approve" data-name="${name}">通过</button><button class="reject" data-action="reject" data-name="${name}">拒绝</button>`
                : "";
            return `<tr><td><div class="user"><span class="user-dot">${initial}</span><div><strong>${name}</strong></div></div></td><td><div class="reason" title="${escapeHtml(
              x.reason,
            )}">${escapeHtml(x.reason)}</div></td><td>${escapeHtml(x.contact)}</td><td>${escapeHtml(
              x.time,
            )}</td><td><span class="badge ${x.status}">${
              labels[x.status]
            }</span></td><td class="invite-code">${
              x.status === "approved" ? escapeHtml(x.code || "—") : "—"
            }</td><td><div class="actions">${actions}<button class="view" data-action="view" data-name="${name}">查看</button></div></td></tr>`;
          })
          .join("");

        document.getElementById("empty").style.display = list.length
          ? "none"
          : "block";
        document.getElementById("resultCount").textContent =
          `显示 ${list.length} 条申请`;

        const countBy = (status) =>
          applications.filter((x) => x.status === status).length;
        document.getElementById("pendingCount").textContent = countBy("pending");
        document.getElementById("approvedCount").textContent =
          countBy("approved");
        document.getElementById("rejectedCount").textContent =
          countBy("rejected");
        document.getElementById("totalCount").textContent = applications.length;
      }

      async function setStatus(name, status) {
        const url =
          status === "approved"
            ? `/api/user/approve/${encodeURIComponent(name)}`
            : `/api/user/reject/${encodeURIComponent(name)}`;
        try {
          const code = await request(url, { method: "POST" });
          if (status === "approved") {
            showToast(code ? `申请已通过，邀请码：${code}` : "申请已通过");
          } else {
            showToast("申请已拒绝");
          }
          await loadApplications();
        } catch (error) {
          console.error(error);
          showToast("操作失败：" + error.message);
        }
      }

      function showToast(msg) {
        const t = document.getElementById("toast");
        t.textContent = msg;
        t.classList.add("show");
        setTimeout(() => t.classList.remove("show"), 2200);
      }

      // 事件委托绑定按钮，避免把用户名拼进 onclick
      rows.addEventListener("click", (event) => {
        const button = event.target.closest("button[data-action]");
        if (!button) {
          // 点击申请原因可在「3 行省略」和「展开全文」之间切换
          const reason = event.target.closest(".reason");
          if (reason) reason.classList.toggle("expanded");
          return;
        }
        const name = button.dataset.name;
        const action = button.dataset.action;
        if (action === "view") {
          const item = applications.find((x) => x.name === name);
          showToast(item ? `${item.name} · ${item.contact}` : "未找到该申请");
          return;
        }
        setStatus(name, action === "approve" ? "approved" : "rejected");
      });

      document.getElementById("search").addEventListener("input", render);
      document.getElementById("status").addEventListener("change", render);
      document.getElementById("logout").addEventListener("click", async () => {
        try {
          await fetch("/api/manager/logout", { method: "POST" });
        } catch (error) {
          console.error(error);
        }
        location.href = "/managerLogin.html";
      });

      document.getElementById("refresh").addEventListener("click", async () => {
        if (await loadApplications()) {
          showToast("列表已刷新");
        }
      });

      loadApplications();
