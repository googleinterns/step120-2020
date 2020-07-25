import React from 'react';

/** Functional Component that creates an input field. Takes in a fieldName and fieldType as props
to fill in the name and type attributes of the input element, respectively */
function InputField(props) {
    const defaultValue = setDefault(props.fieldType);
    return (
        <div>
            <p>{props.fieldHeader}</p>
<<<<<<< HEAD:src/MakeListingDisplay/components/InputField.jsx
            <input name={props.fieldName} type={props.fieldType} min={defaultValue} defaultValue={defaultValue} step={props.fieldStep} required/>
=======
            <input name={props.fieldName} type={props.fieldType} onChange={props.onChange}required/>
>>>>>>> Implementtesting:src/PostListingDisplay/components/InputField.jsx
        </div>
    );
}

function setDefault(type){
    switch(type){
        case "number":
            return "0";
        case "text":
            return "None";
        case "date":
            const today = new Date();
            let day = today.getDate();
            let month = today.getMonth();
            const year = today.getFullYear();
            if(day < 10) {
                day = '0' + day;
            } 
            if(month < 10) {
                month = '0' + month;
            } 
            return year + '-' + day + '-' + month;
        default:
            return "N/A";
    }
}

export default InputField;