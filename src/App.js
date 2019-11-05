import React from 'react';
import Search from './components/Search';
import ViewBuilding from './components/ViewBuilding';
import BuildingList from './components/BuildingList';
import AddBuilding from './components/AddBuilding';
import RemoveBuilding from './components/RemoveBuilding';
import Credit from './components/Credit';
import appStyle from './App.css'

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      filterText: '',
      selectedBuilding: 0
    };
  }

  filterUpdate(value) {
      //Here you will need to set the filterText property of state to the value passed into this function
      this.setState({
          filterText: value
      })
  }

  selectedUpdate(id) {
      //Here you will need to update the selectedBuilding property of state to the id passed into this function
      this.setState({
          selectedBuilding: id
      })
  }

  addData(idVal, codeVal, nameVal, latVal, longVal, addressVal) {
      this.props.data.unshift({"id": idVal, "code": codeVal, "name": nameVal, coordinates:{"latitude":latVal, "longitude":longVal}, "address": addressVal})
      this.forceUpdate()
  }

  removeData(idVal) {
      var i;
      console.log('Removing ' + idVal)
      for (i = 0; i < this.props.data.length; i++) {
          console.log('Pos ' + i + ' ID ' + this.props.data[i].id + ' CurrVal ' + idVal)
          if (this.props.data[i].id === idVal) {
              console.log('found')
              this.props.data.splice(i,1);
              this.forceUpdate()
              return
          }
      }
  }
  
  render() {

    return (
      <div className="bg">
        <div className="row">
          <h1>UF Directory App</h1>
        </div>

        <Search 
          filterText={this.state.filterText}
          filterUpdate={this.filterUpdate.bind(this)}
        />
        <main>
          <div className="row">
            <div className="column1">
              <div className="tableWrapper">
                <table className="table table-striped table-hover">
                  <tbody>
                  <tr>
                    <td>
                      <b>Code Building</b>
                    </td>
                  </tr>
                  <BuildingList
                      data={this.props.data}
                      filterText={this.state.filterText}
                      selectedBuilding={this.state.selectedBuilding}
                      selectedUpdate={this.selectedUpdate.bind(this)}
                  />
                  </tbody>
                </table>
              </div>
            </div>
            <div className="column2">
              <ViewBuilding 
                data={this.props.data}
                currID={this.state.selectedBuilding}
              />
              <AddBuilding
                data={this.props.data}
                addData={this.addData.bind(this)}
              />
              <RemoveBuilding
                currID={this.state.selectedBuilding}
                removeData={this.removeData.bind(this)}
              />
            </div>
          </div>
          <Credit />
        </main>
      </div>
    );
  }
}

export default App;
