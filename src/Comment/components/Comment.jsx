import React from 'react';
import '../styles/Comment.css';

const Comment = (props) => (
    <div className="comment-body">
        { props.commentBody }
    </div>
);

export default Comment;