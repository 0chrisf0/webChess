import './../styles/Menu.css'
import { Client } from '@stomp/stompjs';

function Menu({ startGame }) {

    const stompClient = new Client({
        brokerURL: 'ws://localhost:8080/gs-guide-websocket'}
    );

    stompClient.onConnect =(frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/game', (message) => {
            console.log('Hello');})
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



    const handleStartGame = () => {
        stompClient.activate();
        startGame();
    }

    return (
        <>
            <div className="PlayerNameDiv">
                <h1>Web Chess</h1>
                <input type="text" placeholder="Name"></input>
            </div>

            <div className="CreateGameDiv" onClick={handleStartGame}>
                <input type="submit" value={"Create Game"}></input>
            </div>

            <div className="ConnectGameDiv" onClick={handleStartGame}>
                <input type="text" placeholder="GameID"></input>
                <input type="submit" value={"Connect to Game"}></input>
            </div>
        </>
    )
}

export default Menu