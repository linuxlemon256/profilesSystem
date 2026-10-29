async function login() {
        const username = document.getElementById("theProfiles").value.trim();
        const code = document.getElementById("theInviteCode").value.trim();
        const errorBox = document.getElementById("loginError");
        const button = document.getElementById("submitLogin");

        errorBox.classList.remove("show");

        if (!username || !code) {
          errorBox.textContent = "请填写账户和邀请码";
          errorBox.classList.add("show");
          return;
        }

        button.disabled = true;
        try {
          const response = await fetch("/api/user/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username: username, code: code }),
          });
          const result = await response.json();

          if (result.code === 200) {
            try {
              sessionStorage.setItem("username", result.data);
            } catch (e) {
              console.warn("sessionStorage 不可用", e);
            }
            location.href = "learnServer.html?name=" + encodeURIComponent(result.data);
          } else {
            errorBox.textContent = result.message || "登录失败，请稍后重试";
            errorBox.classList.add("show");
          }
        } catch (error) {
          console.error("Error:", error);
          errorBox.textContent = "网络异常，请稍后重试";
          errorBox.classList.add("show");
        } finally {
          button.disabled = false;
        }
      }
