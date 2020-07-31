import React from 'react';

/** Functional Component that creates an input field. Takes in a fieldName and fieldType as props
to fill in the name and type attributes of the input element, respectively */
function InputField(props) {
    const defaultValue = setDefault(props.fieldType);
    return (
        <div>
            <label htmlFor={props.fieldName}>{props.fieldHeader}</label>
            <input 
                id={props.fieldName} 
                name={props.fieldName} 
                type={props.fieldType} 
                min={defaultValue} 
                defaultValue={defaultValue} 
                value={props.fieldValue}
                step={props.fieldStep} 
                onChange={props.onChange} 
                required
            />
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
            let month = today.getMonth()+1;
            const year = today.getFullYear();
            if(day < 10) {
                day = '0' + day;
            } 
            if(month < 10) {
                month = '0' + month;
            } 
            return year + '-' + month + '-' + day;
        default:
            return;
    }
}

export default InputField;