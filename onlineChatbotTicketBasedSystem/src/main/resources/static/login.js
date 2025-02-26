


const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});

 async function handlelogin(event) {

    event.preventDefault();

    console.log('submited');

    const form=event.target;

    const email=form.lemail.value;

    const password=form.lpassword.value;

    console.log(email);
    console.log(password);
    const userData = {
        email:email,
        password:password
    };


         const response = await axios.post('http://localhost:8080/login', userData);

      const token = response.token
         console.log(token)
         if (response.status === 200 && response.data != 0 ) {
             console.log("SUCCESS");
             window.location.href ="index.html"
             alert("you have successfully login")
             const data = response.data; // Handle response data
             console.log("Response Data:", data);
         }
         else {
             // Network errors or other issues
             alert("Incorrect password")
         }

 }

async function handlesignup(event) {

    event.preventDefault();
    console.log('submited');
    const form=event.target;
    const sname=form.sname.value;
    const semail=form.semail.value;
    const spassword=form.spassword.value;
    console.log(sname);
    console.log(semail);
    console.log(spassword);

    const userData = {
        name: sname,
        email: semail,
        password: spassword,

    };

    try {
        const response = await axios.post('http://localhost:8080/signup', userData);
        console.log(response.data); // User created successfully
    } catch (error) {
        console.error('Error:', error.response?.data || error.message);
    }



 }