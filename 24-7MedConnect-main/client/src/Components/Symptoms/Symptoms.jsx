import React, {useState, useRef, useEffect} from 'react';
import './Symptoms.css';
import {symptomsPost} from "../../../api/api.js"

const Symptoms = (props) => {

  const{onClick, onData} = props
  const [symptoms, setSymptoms] = useState("");
  const inputRef = useRef(null);



    useEffect(() => {
      onData(symptoms); 
    }, [symptoms, onData]);

    const handleSubmit = () => {
      onClick(); 
    };
   

  return (
    <div className="symptoms_container">
        <div className='symptoms_form'> 
            <input ref={inputRef} onChange={(e) => setSymptoms(e.target.value)} className="symptoms_input" type='text' placeholder='Please explain your problem briefly'/>
            <button onClick={() => handleSubmit()} className='symptoms_submit'>SUBMIT</button>
        </div>
    </div>
  )
}

export default Symptoms