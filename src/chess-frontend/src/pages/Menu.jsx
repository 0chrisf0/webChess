import './../styles/Menu.css'

function Menu({ startGame }) {

    const handleStartGame = () => {
        startGame()
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