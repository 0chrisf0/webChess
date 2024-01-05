import { useState } from 'react'

import Game from './pages/Game.jsx'
import Menu from './pages/Menu.jsx'
import {Client} from "@stomp/stompjs";
import axios from "axios";


const stompClient = new Client({
  brokerURL: 'ws://localhost:8080/gs-guide-websocket'}
);

stompClient.onConnect = (frame) => {
  console.log('Connected: ' + frame);
}

stompClient.onStompError = function (frame) {
  // Will be invoked in case of error encountered at Broker
  // Bad login/passcode typically will cause an error
  // Complaint brokers will set `message` header with a brief message. Body may contain details.
  // Compliant brokers will terminate the connection after any error
  console.log('Broker reported error: ' + frame.headers['message']);
  console.log('Additional details: ' + frame.body);
};

stompClient.onWebSocketError = (error) => {
  console.error('Error with websocket', error);
};

const App = () => {
  const [gameStart, setGameStart] = useState(false)

  const startGame = () => {
    setGameStart(true)
  }
  const endGame = () => {
    setGameStart(false)
  }

  const handleCreateGame = async (name) => {
    stompClient.activate();
    const response = await axios.post(`/api/create`, {
    name,
    id: 'PLACEHOLDER',
    color: -1});
    let data = response.data;
    stompClient.gameId = data.gameId;
    stompClient.playerColor = -1;
    stompClient.initialState = data.state;
    startGame();
  }

  const handleConnectGame = async (inputID, name) => {
    console.log("CONNECTING: " + inputID + " " + name)
    axios.post(`/api/connect`, {
      player : {
        name,
        id : "placeholder",
        color : 1
      },
      gameId : inputID
    })
        .then((response) => {
          stompClient.activate();
          stompClient.gameId = inputID;
          stompClient.playerColor = 1;
          stompClient.initialState = response.data
          startGame();
        } )
        .catch((error) => console.log(error.message))
  }

  const gameProps={endGame, stompClient}
  const menuProps={startGame, handleCreateGame, handleConnectGame, stompClient}

  return (
    <>
      {gameStart ? <Game props={gameProps}/> : <Menu props={menuProps}/>}
    </>
  )
}

export default App