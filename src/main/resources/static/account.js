async function loadAccount() {
  try {
    const response = await fetch("/api/user/me");
    if (response.status === 401) {
      location.href = "login.html";
      return;
    }
    const result = await response.json();
    if (result.code !== 200 || !result.data) throw new Error(result.message || "账户信息读取失败");
    const user = result.data;
    document.getElementById("accountName").textContent = user.username || "未填写";
    document.getElementById("welcome").textContent = "你好，" + (user.username || "成员");
    document.getElementById("accountStatus").textContent = user.isin === 1 ? "已通过审核" : "审核中";
    document.getElementById("accountEmail").textContent = user.email || "未填写";
    document.getElementById("accountReason").textContent = user.reason || "未填写";
    document.getElementById("accountCreatedAt").textContent = user.createdAt ? new Date(user.createdAt).toLocaleDateString("zh-CN") : "—";
  } catch (error) {
    console.error(error);
    document.getElementById("accountStatus").textContent = "读取失败";
  }
}

loadAccount();
