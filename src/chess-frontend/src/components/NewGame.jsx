import React from 'react'
import './../styles/Menubar.css'

const NewGame = ({onClick}) => {
    return (
            <button className="menu-button" onClick={onClick}>Import FEN</button>
    )
}

export default NewGame
