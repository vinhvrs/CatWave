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
      setCookie(member.uid);  // set cookie for session
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

async function setCookie(uid){
  const res = await fetch("/api/session/setCookie",{
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({ uid: uid })
  });
  if (!res.ok) throw new Error("Failed to set cookie");
  const data = await res.json();
  console.log("Cookie set:", data); 
}

async function fetchCurrentUser() {
  const res = await fetch("/api/session/getCookie", {
    method: "GET",
    credentials: "include"          // ← must also include cookies here
  });
  if (!res.ok) {
    console.log("No valid session, user not logged in");
    return null;
  }
  return await res.json();         // maybe returns { uid, username, email, … }
}

onload = async () => {
  let user = await fetchCurrentUser();
  if (user) {
    // user is logged in, redirect to home
    window.location.href = "/home";
  } else {
    // user is not logged in, show login form
    document.getElementById("loginForm").style.display = "block";
  }
  };