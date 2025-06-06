onload = async function() {
  try {
    await this.fetch(`/api/session/validateCookie`, {
      method: "GET",
      credentials: "include",
    }).then(async (res) => {
      if (res.ok) {
        const data = await res.json();
        this.localStorage.setItem("uid", data.uid);
        this.localStorage.setItem("username", data.username);

        // redirect to home if already logged in
        window.location.href = "/home";
      } else if (res.status === 401) {
        console.log("No valid session cookie found, proceeding to login.");
      }
    });
  }
  catch (e) {
    console.error("Error validating session cookie:", e);
  }
}


async function checkLogin() {
  // grab the credentials
  const userLogin = document.getElementById("username").value.trim();
  const passLogin = document.getElementById("password").value;

  if (!userLogin || !passLogin) {
    alert("Username and password cannot be empty");
    return false;   // prevent actual form‐submit
  }

  try {
    const res = await fetch("/api/auth/login", {
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
      localStorage.setItem("username", member.username);
      await setCookie(member.uid);  // set cookie for session
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

  return true;  // always prevent the browser’s default form submit
}

async function setCookie(uid) {
  const res = await fetch(`/api/session/setCookie/${uid}`, {
    method: "GET",
    credentials: "include",
  });
  if (!res.ok) throw new Error(res.statusText);
  const data = await res.json();
  console.log("Cookie set:", data);
}
