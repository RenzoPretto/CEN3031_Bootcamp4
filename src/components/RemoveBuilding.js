import React from 'react';

class RemoveBuilding extends React.Component {
    removeData() {
        this.props.removeData(this.props.currID)
    }
    render() {
        return (
            <div>   
                <button type="button" class="btn btn-addBuild" onClick={() => this.removeData()}>Remove Building</button>
            </div>
        )
    }
}
export default RemoveBuilding;