import React, {useRef, useState} from 'react'; 
import Symptoms from '../Components/Symptoms/Symptoms';
import '../App.css';
import Nav from '../Components/Nav/Nav';
import DocInfo from '../Components/DocInfo/DocInfo';

import { symptomsPost } from '../../api/api';


const Home = () => {

  const [symptoms, setSymptoms] = useState(0);
  const [doctors, setDocs] = useState([]);
  const [showRef, setShowRef ]= useState(true);


  const handleSubmitClick = async() => {
    try {

      if(symptoms === ""){
        window.alert("BAD");
      }
      else{
      await symptomsPost({symptoms})
      .then((res) => {
        setDocs(res.data.doctors);
        setShowRef(false);
      })}
    } catch (error) {
      
    }
  }

  const handleInputData = (data) => {
    setSymptoms(data);
  }
  
  return (
    <div className="mainContainer">
        <Nav/>
        {showRef && <Symptoms onData={handleInputData} onClick={handleSubmitClick}/>}

        <div className='containerWrapper'>
        
       {doctors.map((doctor) => (
               
             <DocInfo Name={doctor.Name} Speciality={doctor.Specialty} Status={doctor.Online_Status} Languages={doctor.Languages} />
       
         ))}
        </div> 

    </div>
  )
}

export default Home