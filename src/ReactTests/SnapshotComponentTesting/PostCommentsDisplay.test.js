import React from 'react';
import renderer from 'react-test-renderer';
import PostCommentsDisplay from '../../PostCommentsDisplay/components/PostCommentsDisplay.jsx';
import CommentEditor from '../../PostCommentsDisplay/components/CommentEditor.jsx';
import Comment from '../../PostCommentsDisplay/components/Comment.jsx';

test('PostCommentDisplay renders correctly', () => {
    const tree = renderer
        .create(<PostCommentsDisplay />)
        .toJSON();
    expect(tree).toMatchSnapshot();
});

test('CommentEditor renders correctly', () => {
    const tree = renderer
        .create(<CommentEditor />)
        .toJSON();
    expect(tree).toMatchSnapshot();
});

test('Comment renders correctly', () => {
    const tree = renderer
        .create(<Comment commentBody="Hello There!"/>)
        .toJSON();
    expect(tree).toMatchSnapshot();
});