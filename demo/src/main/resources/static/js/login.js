async function checkLogin() {
    // grab the credentials
    const userLogin = document.getElementById("username").value.trim();
    const passLogin = document.getElementById("password").value;

    if (!userLogin || !passLogin) {
      alert("Username and password cannot be empty");
      return false;   // prevent actual form‐submit
    }

    try {
      const res = await fetch("/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: userLogin,
          password: passLogin
        })
      });

      if (res.ok) {
        // on success you return the Member JSON
        const member = await res.json();
        localStorage.setItem("uid", member.uid);
        // redirect or update UI
        window.location.href = "/home";
      } else if (res.status === 401) {
        alert("Invalid credentials");
      } else {
        const err = await res.text();
        alert("Login error: " + err);
      }
    } catch (e) {
      console.error("Fetch failed:", e);
      alert("Network or server error");
    }

    return false;  // always prevent the browser’s default form submit
  }