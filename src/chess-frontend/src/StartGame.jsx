import React from 'react';
import './Menubar.css'

const StartGame = ({onClick}) => {
    return (
        <button className="menu-button" onClick={onClick}>Start</button>
    );
};

export default StartGame;
