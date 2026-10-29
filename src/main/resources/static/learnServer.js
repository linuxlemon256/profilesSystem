const name = new URLSearchParams(location.search).get("name");
      if (name) {
        document.getElementById("user").textContent = "你好，" + name;
        document.getElementById("accountLink").href =
          "account.html?name=" + encodeURIComponent(name);
      }
