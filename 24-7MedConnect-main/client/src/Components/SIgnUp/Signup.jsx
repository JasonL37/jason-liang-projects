import React,{useLayoutEffect, useRef, useState} from 'react'; 
import './SignUp.css';
import { registerDoctor} from '../../../api/api.js';
import { useNavigate } from 'react-router-dom';



import signUp from '../../Utils/Images/signUp.jpg';

const Signup = () => {

  const [up, setUp] = useState(true);
  const nameRef = useRef(null);
  const emailRef = useRef(null);
  const passwordRef = useRef(null);
  const specialityRef = useRef(null);
  const languageRef = useRef(null);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [speciality, setSpeciality] = useState("");
  const [language, setLanguage] = useState("");

  const navigate = useNavigate();


  const emailRefIn = useRef(null);
  const passwordRefIn = useRef(null);
  const [patient, setPatient] = useState(true);


  const DoctorSignIn = {
    display:  patient === true ? 'none' : '',  // Change color based on Status
  };



  const signInUp = {
    display:  up === true ? 'none' : '',  // Change color based on Status
  };

  const signUpIn = {
    display:  up === false ? 'none' : '',  // Change color based on Status
  };


    const onSignUpClick = async() => {
      if(nameRef.current.value == "" || emailRef.current.value == "" || passwordRef.current.value == "")
      {
        window.alert("Please fill in details");
      }; 

      await registerDoctor({name, email, password, speciality, language})
      .then((res) => {  
        console.log(res);
      }); 

      

    }

      const onSignInClick = () => {
      if(emailRefIn.current.value == "" || passwordRefIn.current.value == "")
      {
        window.alert("Please fill in details");
      }
      else{
        navigate('/home');
      }
  }


  return (
    <div className="container">
        <div className="left">
              <img src={signUp}/>
        </div>

        <div className="right">
          <h1>Welcome to 24/7 MedConnect</h1>
          

        <div className="signUp" style={signUpIn}>
        <h3>Please Sign Up With Your details</h3>
          <div className='inputContainer'>
            <label for="name">Name</label>
            <input onChange={(e) => setName(e.target.value)} ref={nameRef} name="name" placeholder='Please enter your Full Name'></input>
          </div>

          <div className='inputContainer'>
            <label for="name">Email</label>
            <input onChange={(e) => setEmail(e.target.value)} ref={emailRef} name="name"placeholder='Please enter your Email'/>
          </div>

          <div className='inputContainer'>
            <label for="name">Password</label>
            <input onChange={(e) => setPassword(e.target.value)} ref={passwordRef} name="name" type="password" placeholder='Please enter your Password'></input>
          </div>
          
          <div className='inputContainer'  style={DoctorSignIn}>
            <label for="Specialty">Specialty</label>
            <input onChange={(e) => setSpeciality(e.target.value)} ref={specialityRef} name="Specialty"placeholder='Please enter your Speciality'/>
          </div>

          <div className='inputContainer'  style={DoctorSignIn}>
            <label for="Languages">Languages</label>
            <input onChange={(e) => setLanguage(e.target.value)} ref={languageRef}  name="Languages" placeholder='Please enter your Languages'></input>
          </div>

          <div className='doctorPatient'>
            <button onClick={() => setPatient(false)}>Doctor</button>
            <button onClick={() => setPatient(true)}>Patient</button>
          </div>

          <div className='inputContainer'>
           <button onClick={() => onSignUpClick()}>Sign Up</button>
          </div>

          

          <div className='bottomButton'>
            <h4>Already a member?</h4>
            <button onClick={() => setUp(!up)}>SignIn</button>
          </div>

       
        </div>

        <div className="signUp" style={signInUp}>
        <p>Please Sign In With Your details</p>
          <div className='inputContainer'>
            <label for="email">Email</label>
            <input ref={emailRefIn} name="email"placeholder='Please enter your Email'/>
          </div>

          <div className='inputContainer'>
            <label for="name">Password</label>
            <input ref={passwordRefIn} name="name" type="password"  placeholder='Please enter your Password'></input>
          </div>

          <div className='inputContainer'>
          <button onClick={() => onSignInClick()}>Sign In</button>
          </div>

          <div className='bottomButton'>
            <h4>Not a member?</h4>
            <button onClick={() => setUp(!up)}>SignUp</button>
          </div>

          </div>
      </div>
    </div>
  )
}

export default Signup