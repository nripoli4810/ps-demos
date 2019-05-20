var PropTypes = require('prop-types')
var React = require('react')

var api = require('../utils/api')
var Loading = require('./loading')

function SelectLanguage(props) {
    var languages = ['All', 'JavaScript', 'Ruby', 'Java', 'CSS', 'Python']
    return (
        <ul className='languages'>
            {languages.map(function (lang) {
                return (
                    <li key={lang}
                        style={lang === props.selectedLanguage ? { color: '#D0021B' } : null}
                        onClick={props.onSelect.bind(null, lang)} >
                        {lang}
                    </li>
                )
            }, this)}
        </ul>
    )
}

function RepositoryGrid(props) {
    return (
        <ul className='popular-list'>
            {props.repositories.map(function (repo, index) {
                return (
                    <li key={repo.name} className='popular-item'>
                        <div className='popular-rank'>#{index + 1}</div>
                        <ul className='space-list-items'>
                            <li>
                                <img className='avatar'
                                    src={repo.owner.avatar_url}
                                    alt={'Avatar for ' + repo.owner.login} />
                            </li>
                            <li>
                                <a href={repo.html_url}>{repo.name}</a>
                            </li>
                            <li>@{repo.owner.login}</li>
                            <li>{repo.stargazers_count} stars</li>
                        </ul>
                    </li>
                )
            })}
        </ul>
    )
}

RepositoryGrid.propTypes = {
    repositories: PropTypes.array.isRequired
}

SelectLanguage.propTypes = {
    selectedLanguage: PropTypes.string.isRequired,
    onSelect: PropTypes.func.isRequired
}

class Popular extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            selectedLanguage: 'All',
            repositories: null
        }

        this.updateLanguage = this.updateLanguage.bind(this)
    }

    componentDidMount() {
        this.updateLanguage(this.state.selectedLanguage)
    }

    updateLanguage(lang) {
        this.setState(function () {
            return {
                selectedLanguage: lang,
                repositories: null
            }
        });

        api.fetchPopularRepos(lang)
            .then(function (repositories) {
                this.setState(function () {
                    return {
                        repositories: repositories
                    }
                })
            }.bind(this))
    }

    render() {
        return (
            <div>
                <SelectLanguage selectedLanguage={this.state.selectedLanguage}
                    onSelect={this.updateLanguage} />
                {!this.state.repositories
                    ? <Loading text='Downloading' />
                    : <RepositoryGrid repositories={this.state.repositories} />
                }
            </div>
        )
    }
}

module.exports = Popular;