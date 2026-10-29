const applyForm = document.getElementById("applyForm");
      const applySuccess = document.getElementById("applySuccess");
      const applyError = document.getElementById("applyError");
      const applyBtn = applyForm.querySelector(".apply-btn");

      const EMAIL_PATTERN = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

      applyForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        applyError.classList.remove("show");

        const payload = {
          username: document.getElementById("applyName").value.trim(),
          reason: document.getElementById("applyReason").value.trim(),
          email: document.getElementById("applyContact").value.trim(),
        };

        // 邮箱必填：先查空，再查格式，后端也会再校验一次
        if (!payload.email) {
          applyError.textContent = "请填写邮箱";
          applyError.classList.add("show");
          return;
        }
        if (!EMAIL_PATTERN.test(payload.email)) {
          applyError.textContent = "邮箱格式不正确，例如 name@example.com";
          applyError.classList.add("show");
          return;
        }

        applyBtn.disabled = true;

        try {
          const response = await fetch("/api/user/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
          });

          const result = await response.json();

          if (result.code === 200) {
            applyForm.style.display = "none";
            applySuccess.classList.add("show");
          } else {
            applyError.textContent = result.message || "提交失败，请稍后重试";
            applyError.classList.add("show");
          }
        } catch (error) {
          console.error("Error:", error);
          applyError.textContent = "网络异常，请稍后重试";
          applyError.classList.add("show");
        } finally {
          applyBtn.disabled = false;
        }
      });
