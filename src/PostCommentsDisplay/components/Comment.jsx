import React from 'react';
import '../styles/Comment.css';

/** Stateless functional component that displays the text of the comment. 
Takes in a string called commentBody, which is the text that gets displayed */
const Comment = (props) => (
    <div className="comment-body">
        { props.commentBody }
    </div>
);

export default Comment;