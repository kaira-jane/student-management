function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
  
    // Dummy credentials
    const correctUsername = "admin";
    const correctPassword = "12345";
  
    if (username === correctUsername && password === correctPassword) {
      document.getElementById('message').style.color = "green";
      document.getElementById('message').innerText = "Login successful!";
      // You can redirect to another page here:
      // window.location.href = "dashboard.html";
    } else {
      document.getElementById('message').style.color = "red";
      document.getElementById('message').innerText = "Invalid username or password";
    }
  }