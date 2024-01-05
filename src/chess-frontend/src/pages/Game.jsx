import { useState, useEffect } from 'react';
import axios from 'axios';
import Chessboard from '../components/Chessboard.jsx'
import Menubar from '../components/Menubar.jsx'

import { Client } from '@stomp/stompjs';


const Game = ({props}) => {
    const {endGame, stompClient} = props;



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

    useEffect (() => {setTimeout(function() {
        console.log("SUBSCRIBING: /topic/game/" + stompClient.gameId);

        let color = stompClient.playerColor;
        if (color === -1) {
            setBoardstate(stompClient.initialState.boardstate)
            setHighlights(stompClient.initialState.highlights)
            setGamestate(stompClient.initialState.gamestate)
        } else {
            setBoardstate(stompClient.initialState.boardstate.reverse())
            setHighlights(stompClient.initialState.highlights.reverse())
            setGamestate(stompClient.initialState.gamestate)
        }


        stompClient.subscribe("/topic/game/" + stompClient.gameId, function (response) {
            let data = JSON.parse(response.body);
            if (color === -1) {
                setBoardstate(data.boardstate)
                setHighlights(data.highlights)
                setGamestate(data.gamestate)
            } else {
                setBoardstate(data.boardstate.reverse())
                setHighlights(data.highlights.reverse())
                setGamestate(data.gamestate)
            }
        })

    }, 1000)}, [])



    const handleStartGameClick = async () => {
        try {
            await axios.post(`/api/start`, {
                data : "",
                gameId : stompClient.gameId
            })

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
                await axios.post(`/api/FEN`, {
                    data : inputFEN,
                    gameId : stompClient.gameId
                })


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
            await axios.post(`/api/click`, {
                position,
                gameId : stompClient.gameId,
                color : stompClient.playerColor
            })


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
