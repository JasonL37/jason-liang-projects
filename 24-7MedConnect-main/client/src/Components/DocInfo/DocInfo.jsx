import React from 'react'; 
import './DocInfo.css';
import doc from "../../Utils/Images/doc1.png";



const DocInfo = (props) => {
    const {Name, Speciality, Status, Languages} = props;

    const back = {
        background: Status === 'Online' ? 'linear-gradient(to right, #2e8b57, #40e0d0)' : 'linear-gradient(to right, #d3d3d3, #333333)',  // Change color based on Status
        padding: '10px',
        borderRadius: '5px',
        color: 'white', // Ensures text is readable on the background
      };

      const active = {
        
        background: Status === 'Online' ? 'rgba(17, 83, 191, 0.7)' : 'linear-gradient(to right, #d3d3d3, #333333)',  // Change color based on Status
      };
    

  return (
    <div className="docContainer" style={back} >
        <div className='nameContainer'>
            <h1>{Name}</h1>
        </div>
        <div className="imgContainer">
            <img src={doc}></img>
        </div>
        <div className="infoContainer">
            <h2>{Speciality}</h2>
        </div>

        <div className="infoContainer">
            <h2>{Languages}</h2>
        </div>

        <div className="infoContainer callButton" >
            <button style = {active} className='callButton'>Call</button>
        </div>
    </div>
  )
}

export default DocInfo