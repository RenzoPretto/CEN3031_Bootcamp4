import React, { Component } from 'react';
import './App.css';

class App extends Component {

  state = {
    flowers: [],
    flower: {
      genus: '',
      species: '',
      comname: ''
    }
  }

  componentDidMount() {
    this.getFlowers();
  }

  getFlowers = _ => {
    fetch('http://localhost:4000/flowers')
      .then(response => response.json())
      .then(response => this.setState({ flowers: response.data }))
  }

  addFlower = _ => {
    const { flower } = this.state;
    fetch(`http://localhost:4000/flowers/add?genus=${flower.genus}&species=${flower.species}&comname=${flower.comname}`)
      .then(this.getFlowers)
  }

  renderFlower = ({GENUS, SPECIES, COMNAME}) => <div key={COMNAME}>{GENUS + " " + SPECIES}</div>

  render() {
    const { flowers, flower } = this.state;
    return (
      <div>
        <div className="App">
          {flowers.map(this.renderFlower, console.log('test'))}
        </div>
        <div>
          <input value={flower.comname} onChange={comname => this.setState({ flower: {...flower, comname: comname.target.value}})} />
          <input value={flower.genus} onChange={genus => this.setState({ flower: {...flower, genus: genus.target.value}})}/>
          <input value={flower.species} onChange={species => this.setState({ flower: {...flower, species: species.target.value}})}/>
        </div>
        <button onClick={this.addFlower}>Add Flower</button>
        <button onClick={console.log(this.state.flower)}>Test</button>
      </div>
    )
  }
}

export default App;