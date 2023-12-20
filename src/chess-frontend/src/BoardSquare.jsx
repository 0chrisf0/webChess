import { useState } from 'react'
import './BoardSquare.css'


function BoardSquare ({props}) {
    const { row, col, path, highlight, handleClick } = props;


    const isEvenRow = row % 2 === 0;
    const isEvenCol = col % 2 === 0;


    const squareColor = highlight ? 'highlight' : (isEvenRow ? (isEvenCol ? 'light' : 'dark') : isEvenCol ? 'dark' : 'light');


    return (
        <div>
            <button className={`board-square ${squareColor}`} onClick={() => handleClick(row,col)}>
                {path !== "Empty" && <img src={path} alt={path} className={"centered-image"}/>}
            </button>
        </div>
    )

}

export default BoardSquare