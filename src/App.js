import React, { Component } from 'react';
import Comment from './Comment/components/Comment';
import CommentEditor from './CommentEditor/components/CommentEditor';
import './App.css';

class App extends Component {
    constructor(props) {
        super(props);

        this.addComment = this.addComment.bind(this);

        this.state = {
            comments: [],
        }
    }

    addComment(newCommentBody){
        const newState = Object.assign({}, this.state);
        newState.comments.push(newCommentBody);
        this.setState(newState);
    }

    render(){
        return (
            <div>
                { 
                    this.state.comments.map((commentBody, i) => {
                        return (
                            <Comment key={i} commentBody={commentBody}/>
                        )
                    })
                }

                <CommentEditor addComment={this.addComment} />
            </div>
        );
    }
}

export default App;
