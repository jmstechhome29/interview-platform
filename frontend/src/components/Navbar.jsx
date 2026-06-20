import { useNavigate } from 'react-router-dom'

export default function Navbar({ title }) {
  const navigate = useNavigate()
  const user = JSON.parse(localStorage.getItem('user') || 'null')

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    navigate('/login')
  }

  return (
    <div className="navbar">
      <h1>{title} {user ? `— ${user.fullName}` : ''}</h1>
      <button onClick={logout}>Logout</button>
    </div>
  )
}
