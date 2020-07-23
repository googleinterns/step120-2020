import React from 'react';
import renderer from 'react-test-renderer';
import PostCommentsDisplay from '../PostCommentsDisplay/components/PostCommentsDisplay.jsx';
import CommentEditor from '../PostCommentsDisplay/components/CommentEditor.jsx';
import Comment from '../PostCommentsDisplay/components/Comment.jsx';

test('PostCommentDisplay renders correctly', () => {
  const tree = renderer
    .create(<PostCommentDisplay />)
    .toJSON();
  expect(tree).toMatchSnapshot();
});