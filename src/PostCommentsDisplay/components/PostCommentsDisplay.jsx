import React, { Component } from 'react';
import Comment from './Comment';
import CommentEditor from './CommentEditor';

/** Component Class that displays all of the comments, the input box, and the post button.
 * Handles adding text from the text box to the array of comments. 
 */
class PostCommentsDisplay extends Component {
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
        )
    }
}

export default PostCommentsDisplay;