import React, { Component } from 'react';
import Comment from './Comment/component/Comment';
import './App.css';

class App extends Component {
  constructor(props) {
    super(props);

    this.addComment = this.addComment.bind(this);
    this.handleCommentEditorChange = this.handleCommentEditorChange.bind(this);

    this.state = {
      comments: [],
      newCommentBody: '',
    }
  }

  addComment(){
    const newState = Object.assign({}, this.state);
    newState.comments.push(this.state.newCommentBody);
    newState.newCommentBody = '';
    this.setState(newState);
  }

  handleCommentEditorChange(event){
    this.setState({
      newCommentBody: event.target.value
    })
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
        
        <div className="comment-editor">
          <textarea className="comment-input" value={this.state.newCommentBody} onChange={this.handleCommentEditorChange}/>
          <button className="post-button" onClick={this.addComment}>Post</button>
        </div>
        
      </div>
    );
  }
}

export default App;
