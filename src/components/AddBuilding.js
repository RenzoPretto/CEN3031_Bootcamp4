import React from 'react';

class AddBuilding extends React.Component {
    addData() {
        const idVal = this.refs.id.value
        const codeVal = this.refs.code.value
        const nameVal = this.refs.name.value
        const latVal = this.refs.lat.value
        const longVal = this.refs.long.value
        const addressVal = this.refs.address.value
        console.log('test')
        this.props.addData(idVal, codeVal, nameVal, latVal, longVal, addressVal)
    }
   render() {
        return (
        <div>
            <button type="button" class="btn btn-addBuild" onClick={() => this.addData()}>Add Building</button>
			<form>
				<input
                    type="text"
                    ref="id"
                    placeholder="Type ID"
                />
			</form>
            <form>
				<input
                    type="text"
                    ref="code"
                    placeholder="Type Code" 
                />
			</form>
            <form>
				<input
			        type="text"
			        ref="name"
			        placeholder="Type Name" 
                />
            </form>
            <form>
			    <input
			        type="text"
	                ref="lat"
		            placeholder="Type Latitude" 
                />
            </form>
            <form>
			    	<input
			        type="text"
			        ref="long"
			     placeholder="Type Longitude" 
            />
            </form>
            <form>
			    	<input
			        type="text"
			        ref="address"
			        placeholder="Type Address" 
            />
            </form>
        </div>
		);        
   }
}
export default AddBuilding;