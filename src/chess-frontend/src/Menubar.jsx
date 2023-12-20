import React from 'react';
import NewGame from './NewGame.jsx';
import StartGame from './StartGame.jsx'

const Menubar = ({props}) => {
    const {startgame, newgame, gamestate} = props
    return (
        <div className="menu-bar">
            <StartGame onClick={startgame} />
            <span className="menu-text">{gamestate}</span>
            <NewGame onClick={newgame} />
        </div>
    );
};

export default Menubar;
