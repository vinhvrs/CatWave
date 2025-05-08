function setCookie(){
    fetch("127.0.0.1:1212/auth/setCookie", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "username": "test",
            "password": "123456"
        })
    }).then((response) => {
        if (response.ok) {
            console.log("Cookie set successfully!");
        } else {
            console.error("Failed to set cookie.");
        }
    }
    ).catch((error) => {
        console.error("Error:", error);
    });

}

function getCookie() {

}