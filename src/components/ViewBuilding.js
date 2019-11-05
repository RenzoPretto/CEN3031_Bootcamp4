import React from 'react';

class ViewBuilding extends React.Component {
    render() {
        const { data } = this.props;
        const buildingData = data
        .filter(id => {
            return id.id === this.props.currID
        })
        .map(directory => {
            if (directory.coordinates != null) {
            return (
			    <div>
				    <p>
                        <table cellspacing="10" bgcolor="white" frame="box">
                            <tr key={directory.id}>
                                <td> {directory.code} </td>
                                <td> {directory.name} </td>
                                <td> {directory.address} </td>
                                <td> {directory.coordinates.latitude} </td>
                                <td> {directory.coordinates.longitude} </td>
                            </tr>
                        </table>
	   		        </p>
			    </div>
            );}
            return (
			    <div>
				    <p>
                        <table cellspacing="10" bgcolor="white" frame="box">
                            <tr key={directory.id}>
                                <td> {directory.code} </td>
                                <td> {directory.name} </td>
                                <td> {directory.address} </td>
                            </tr>
                        </table>
	   		        </p>
			    </div>
            );
        });
        return <div>{buildingData}</div>;
    }
}
export default ViewBuilding;
