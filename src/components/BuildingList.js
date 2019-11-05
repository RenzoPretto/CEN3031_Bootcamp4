import React from 'react';

class BuilingList extends React.Component {
    selectedUpdate(id) {
        this.props.selectedUpdate(id)
    }
	render() {
		const { data } = this.props;

		const buildingList = data
            .filter(name => {
                return name.name.toLowerCase().indexOf(this.props.filterText.toLowerCase()) >= 0
            })
            .map(directory => {
                return (
					<bg bgcolor="dodgerblue">
                        <ul onClick={() => this.selectedUpdate(directory.id)}>
							<table>
								<tr key={directory.id}>
									<td>{directory.code} </td>
									<td>{directory.name} </td>
								</tr>
							</table>
                        </ul>
					</bg>
			);
		});

		return <div>{buildingList}</div>;
	}
}
export default BuilingList;
