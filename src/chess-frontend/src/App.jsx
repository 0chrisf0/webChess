import React, { useState } from 'react';
import axios from 'axios';
import Chessboard from './Chessboard.jsx'
import Menubar from './Menubar.jsx'


const App = () => {
    // Set initial boardstate
    const [boardstate, setBoardstate] = useState([
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"]]);

    const [highlights,setHighlights] = useState ([
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false]]);

    const [gamestate,setGamestate] = useState("Inactive");


    const handleStartGameClick = async () => {
        try {
            // Make an HTTP GET request to the server
            const response = await axios.get(`/api/start`);
            setGamestate(response.data);
        } catch (error) {
            console.error('Error fetching data:', error.message);
        }
    }

    const handleNewGameClick = async () => {
        const inputFEN = prompt('Enter a valid FEN');
        if (inputFEN !== null) {
            setGamestate("Inactive");
            try {
                // Make an HTTP GET request to the server
                const response = await axios.get(`/api/FEN?inputFEN=${encodeURIComponent(inputFEN)}`);
                setBoardstate(response.data);


            } catch (error) {
                console.error('Error fetching data:', error.message);
            }
        }
    };

    const handleClick = async (row, col) => {
        try {
            // Make an HTTP GET request to the server
            // What the server needs: the position of the click
            const position = row.toString() + col.toString();
            const response = await axios.get(`/api/click?pos=${position}`);

            // What I need to be returned: the boardstate + What squares should be highlighted
            setBoardstate(response.data.boardstate);
            setHighlights(response.data.highlights);
            setGamestate(response.data.gamestate);

        } catch (error) {
            console.error('Error fetching data:', error.message);
        }
    };

    const props = {gamestate, newgame: handleNewGameClick, startgame:handleStartGameClick}
    const chessboard = {handleClick, boardstate, highlights}

    return (
        <div>
            {/*Pass in on-click action functions*/}
            <Menubar props={props} />
            <Chessboard props={chessboard} />
        </div>
    );
};

export default App;