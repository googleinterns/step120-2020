import React, { Component } from 'react';
import '../styles/CommentEditor.css';

class CommentEditor extends Component {
    constructor(props) {
        super(props);

        this.state = {
            newCommentBody: '',
        };

        this.handleCommentEditorChange = this.handleCommentEditorChange.bind(this);
        this.createComment = this.createComment.bind(this);
    }

    handleCommentEditorChange(event){
        this.setState({
            newCommentBody: event.target.value
        });
    }

    createComment(){
        this.props.addComment(this.state.newCommentBody);
        this.setState({
            newCommentBody: ''
        });
    }

    render(){
        return (
            <div className="comment-editor">
                <textarea className="comment-input" value={this.state.newCommentBody} onChange={this.handleCommentEditorChange}/>
                <button className="post-button" onClick={this.createComment}>Post</button>
            </div>
        )
    }
}

export default CommentEditor;