import { useState, useEffect } from 'react';
import axios from 'axios';
import Chessboard from '../components/Chessboard.jsx'
import Menubar from '../components/Menubar.jsx'

import { Client } from '@stomp/stompjs';


const Game = ({props}) => {
    const {endGame, stompClient} = props;

    // Wait for 3 seconds
    setTimeout(function() {
        stompClient.subscribe("/topic/game", function (response) {
            let data = JSON.parse(response.body);
            setBoardstate(data.boardstate)
            setHighlights(data.highlights)
            setGamestate(data.gamestate)
        })
    }, 2000);

    // Set initial boardstate
    const [boardstate, setBoardstate] = useState([
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"],
        ["Empty","Empty","Empty","Empty","Empty","Empty","Empty","Empty"]])

    const [highlights,setHighlights] = useState ([
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false],
        [false,false,false,false,false,false,false,false]])

    const [gamestate,setGamestate] = useState("Inactive")



    const handleStartGameClick = async () => {
        try {
            // Make an HTTP GET request to the server
            const response = await axios.get(`/api/start`)

        } catch (error) {
            console.error('Error fetching data:', error.message)
        }
    }

    const handleNewGameClick = async () => {
        const inputFEN = prompt('Enter a valid FEN')
        if (inputFEN !== null) {
            setGamestate("Inactive")
            try {
                // Make an HTTP GET request to the server
                const response = await axios.get(`/api/FEN?inputFEN=${encodeURIComponent(inputFEN)}`)


            } catch (error) {
                console.error('Error fetching data:', error.message)
            }
        }
    };

    const handleClick = async (row, col) => {
        try {
            // Make an HTTP GET request to the server
            // What the server needs: the position of the click
            const position = row.toString() + col.toString()
            const response = await axios.get(`/api/click?pos=${position}`)


        } catch (error) {
            console.error('Error fetching data:', error.message)
        }
    }


    const menuProps = {gamestate, newgame: handleNewGameClick, startgame:handleStartGameClick}
    const chessboard = {handleClick, boardstate, highlights}

    return (
        <div>
            {/*Pass in on-click action functions*/}
            <Menubar props={menuProps} />
            <Chessboard props={chessboard} />
        </div>
    )
}

export default Game
