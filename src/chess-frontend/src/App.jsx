import { useState } from 'react'

import Game from './pages/Game.jsx'
import Menu from './pages/Menu.jsx'

const App = () => {
  const [gameStart, setGameStart] = useState(false)
  const startGame = () => {
    setGameStart(true)
  }
  const endGame = () => {
    setGameStart(false)
  }
  
  return (
    <>
      {gameStart ? <Game endGame={endGame}/> : <Menu startGame={startGame}/>}
    </>
  )
}

export default App