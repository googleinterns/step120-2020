import React from 'react';

/**Functional Component that creates an input field. Takes in a fieldName and fieldType as props
to fill in the name and type attributes of the input element, respectively*/
function InputField(props) {
    const min = (props.fieldType === "number")?"0":null;
    return (
        <div>
            <p>{props.fieldHeader}</p>
            <input name={props.fieldName} type={props.fieldType} min={min} step={props.fieldStep} required/>
        </div>
    );
}

export default InputField;