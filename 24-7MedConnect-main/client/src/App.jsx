import React from 'react'; 
import{ useState, useRef, useEffect} from 'react';
import './App.css'; 
import { symptomsPost} from '../api/api';
import VideoChat from './Components/VideoCall/VideoChat';
import MainPage from './Pages/MainPage';


import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './Pages/Home';

import Signup from './Components/SIgnUp/Signup';


function App() {
  const [symptoms, setSymptoms] = useState(0);
  const [doctors, setDocs] = useState([]);
  const [showRef, setShowRef ]= useState(true);

  const handleSubmitClick = async() => {
    try {

      if(symptoms === ""){
        window.alert("BAD");
      }
      await symptomsPost({symptoms})
      .then((res) => {
        setDocs(res.data.doctors);
        setShowRef(false);
      })
    } catch (error) {
      
    }
  }

  const handleInputData = (data) => {
    setSymptoms(data);
  }
  
  useEffect(() => {


  },[]);

  return (

    <BrowserRouter>
      <Routes>
        <Route path="/home" element={<Home />}/>
        <Route path="/" element={<MainPage />}/>
        <Route path="/videoCall" element={<VideoChat />}/>
    </Routes>
    </BrowserRouter>
  )
}

export default App

