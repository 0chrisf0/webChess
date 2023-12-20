import React from 'react';
import axios from 'axios';
import './Menubar.css'

const NewGame = ({onClick}) => {
    return (
            <button className="menu-button" onClick={onClick}>Import FEN</button>
    );
};

export default NewGame;
