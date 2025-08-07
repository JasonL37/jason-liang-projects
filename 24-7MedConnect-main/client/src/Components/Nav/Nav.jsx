import React from 'react'; 
import './Nav.css';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';


const Nav = () => {
  return (
    <div className="nav_container">
        <div className="nav_item">
            <h3>MedConnect</h3>
        </div>
        <div className="nav_item links">
            <a href="#">HOME</a>
            <a href="#">ACCOUNT</a>
            <a href="#">ABOUT US</a>
        </div>
        <div className="nav_item user_icon" >
            <AccountCircleIcon/>
        </div>

    </div>
  )
}

export default Nav