import './../styles/Menu.css'
import { Client } from '@stomp/stompjs';
import {useState} from "react";

function Menu({ props }) {
    const {startGame, handleCreateGame, handleConnectGame, stompClient} = props;
    const [inputId, setInputId] = useState('');


    const handleInputIdChange = (event) => {
        setInputId(event.target.value);
    };

    return (
        <>
            <div className="PlayerNameDiv">
                <h1>Web Chess</h1>
                <input type="text" placeholder="Name"></input>
            </div>

            <div className="CreateGameDiv" onClick={handleCreateGame}>
                <input type="submit" value={"Create Game"}></input>
            </div>

            <div className="ConnectGameDiv">
                <input
                    type="text"
                    placeholder="GameID"
                    value={inputId}
                    onChange={handleInputIdChange}
                />
                <button onClick={() => handleConnectGame(inputId)}>Connect</button>
            </div>
        </>
    )
}

export default Menu