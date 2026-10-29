const name = new URLSearchParams(location.search).get("name");
      if (name) {
        document.getElementById("accountName").textContent = name;
        document.getElementById("welcome").textContent = "你好，" + name;
      }
