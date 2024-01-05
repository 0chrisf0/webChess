import React from 'react'
import NewGame from './NewGame.jsx'
import StartGame from './StartGame.jsx'

const Menubar = ({props}) => {
    const {startgame, newgame, gamestate, gameId} = props
    return (
        <div className="menu-bar">
            <StartGame onClick={startgame} />
            <span className="menu-text">{gamestate}</span>
            <div>
                <span className="game-id-text">{`GameID: ${1}`}</span>
                <NewGame onClick={newgame} />
            </div>
        </div>
    );
};

export default Menubar
