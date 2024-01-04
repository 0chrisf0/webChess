import './../styles/Menu.css'
import { Client } from '@stomp/stompjs';

function Menu({ props }) {
    const {startGame, handleCreateGame, stompClient} = props;

    return (
        <>
            <div className="PlayerNameDiv">
                <h1>Web Chess</h1>
                <input type="text" placeholder="Name"></input>
            </div>

            <div className="CreateGameDiv" onClick={handleCreateGame}>
                <input type="submit" value={"Create Game"}></input>
            </div>

            <div className="ConnectGameDiv" onClick={handleCreateGame}>
                <input type="text" placeholder="GameID"></input>
                <input type="submit" value={"Connect to Game"}></input>
            </div>
        </>
    )
}

export default Menu