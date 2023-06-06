document.addEventListener('DOMContentLoaded', function() {
  const eye = document.getElementById('eye');
  const password = document.getElementById('password');
  const eyeConfirm = document.getElementById('eye-confirm');
  const passwordConfirm = document.getElementById('password-confirm');
  
  eye.addEventListener("click", function(){
      this.className = this.className === "zmdi zmdi-eye" ? "zmdi zmdi-eye-off" : "zmdi zmdi-eye"
      const type = password.getAttribute("type") === "password" ? "text" : "password"
      password.setAttribute("type", type)
  })
  
  eyeConfirm.addEventListener("click", function(){
      this.className = this.className === "zmdi zmdi-eye" ? "zmdi zmdi-eye-off" : "zmdi zmdi-eye"
      const type = passwordConfirm.getAttribute("type") === "password" ? "text" : "password"
      passwordConfirm.setAttribute("type", type)
  })
});