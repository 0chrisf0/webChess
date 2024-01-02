import React from 'react';
import BoardSquare from './BoardSquare';
import './styles/Chessboard.css'

const Chessboard = ({props}) => {
    const {boardstate, highlights, handleClick} = props;

    const squares = () => {
        const squares = [];

        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {


                const props = { row, col, path: boardstate[row][col], highlight: highlights[row][col], handleClick  }
                squares.push(
                    <div className="grid-item" key={`${row}-${col}`}>
                        <BoardSquare props={props} />
                    </div>
                );

            }
        }

        return squares;
    };

    return (
        <div className="grid-container">
            {squares()}
        </div>
    );
};

export default Chessboard;
