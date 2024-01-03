import './styles/Menu.css';

function Menu() {
    return (
        <>
            <div className="PlayerNameDiv">
                <h1>Web Chess</h1>
                <input type="text" placeholder="Name"></input>
            </div>

            <div className="CreateGameDiv">
                <input type="submit" value={"Create Game"}></input>
            </div>

            <div className="ConnectGameDiv">
                <input type="text" placeholder="GameID"></input>
                <input type="submit" value={"Connect to Game"}></input>
            </div>
        </>
    );
}

export default Menu;