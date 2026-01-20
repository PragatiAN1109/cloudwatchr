# Contributing to CloudWatchr

Thank you for your interest in contributing to CloudWatchr! This document provides guidelines and steps for contributing.

## Code of Conduct

By participating in this project, you agree to maintain a respectful and collaborative environment.

## How to Contribute

### Reporting Bugs

1. Check if the bug has already been reported in [Issues](https://github.com/PragatiAN1109/cloudwatchr/issues)
2. If not, create a new issue with:
   - Clear, descriptive title
   - Detailed description of the bug
   - Steps to reproduce
   - Expected vs actual behavior
   - Environment details (OS, Java version, Node version)
   - Screenshots if applicable

### Suggesting Features

1. Check existing [Issues](https://github.com/PragatiAN1109/cloudwatchr/issues) for similar suggestions
2. Create a new issue with:
   - Clear description of the feature
   - Use case and benefits
   - Possible implementation approach

### Pull Requests

1. **Fork the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/cloudwatchr.git
   cd cloudwatchr
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Follow coding standards (see below)
   - Add tests for new functionality
   - Update documentation as needed

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add amazing feature"
   ```
   
   Use conventional commit messages:
   - `feat:` - New feature
   - `fix:` - Bug fix
   - `docs:` - Documentation changes
   - `style:` - Code style changes (formatting, etc.)
   - `refactor:` - Code refactoring
   - `test:` - Adding or updating tests
   - `chore:` - Maintenance tasks

5. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request**
   - Go to the original repository
   - Click "New Pull Request"
   - Select your branch
   - Fill in the PR template
   - Link related issues

## Development Guidelines

### Backend (Spring Boot)

#### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise
- Use constructor injection for dependencies

#### Example:
```java
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    
    private final MetricsService metricsService;
    
    /**
     * Constructor injection of dependencies
     */
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }
    
    /**
     * Retrieves all metrics
     * @return List of metrics
     */
    @GetMapping
    public ResponseEntity<List<Metric>> getAllMetrics() {
        return ResponseEntity.ok(metricsService.getAllMetrics());
    }
}
```

#### Testing
- Write unit tests for services
- Write integration tests for controllers
- Aim for >80% code coverage
- Use meaningful test names

```java
@Test
void shouldReturnAllMetricsWhenGetAllMetricsCalled() {
    // Given
    List<Metric> expectedMetrics = Arrays.asList(new Metric());
    when(metricsService.getAllMetrics()).thenReturn(expectedMetrics);
    
    // When
    ResponseEntity<List<Metric>> response = controller.getAllMetrics();
    
    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedMetrics, response.getBody());
}
```

#### Building and Testing
```bash
cd <service-directory>
mvn clean install
mvn test
```

### Frontend (Next.js)

#### Code Style
- Use TypeScript for type safety
- Follow React best practices
- Use functional components with hooks
- Keep components small and reusable
- Use Tailwind CSS for styling

#### Example:
```typescript
interface MetricCardProps {
  title: string
  value: number
  unit: string
}

export default function MetricCard({ title, value, unit }: MetricCardProps) {
  return (
    <div className="p-4 border rounded-lg">
      <h3 className="text-lg font-semibold">{title}</h3>
      <p className="text-2xl">
        {value} <span className="text-sm">{unit}</span>
      </p>
    </div>
  )
}
```

#### Testing
- Write component tests using React Testing Library
- Test user interactions
- Mock API calls

#### Linting
```bash
cd frontend
npm run lint
npm run type-check
```

## Project Structure

### Adding a New Backend Service

1. Create service directory: `new-service/`
2. Add `pom.xml` with proper dependencies
3. Create Spring Boot application class
4. Add `application.yml` with configuration
5. Create controller package with endpoints
6. Add health endpoint `/api/health`
7. Update gateway routing in `gateway-service/application.yml`
8. Update README with new service information

### Adding a New Frontend Page

1. Create page in `frontend/app/page-name/`
2. Add `page.tsx` with page component
3. Update navigation in `components/Navigation.tsx`
4. Add route to navigation links
5. Update frontend README if needed

## Documentation

### Code Comments
- Add comments for complex logic
- Document public APIs
- Explain "why" not "what"

### README Updates
- Update README when adding features
- Include usage examples
- Update troubleshooting section

### API Documentation
- Update `docs/API.md` for new endpoints
- Include request/response examples
- Document error codes

## Testing Checklist

Before submitting a PR, ensure:

- [ ] All tests pass
- [ ] New tests added for new functionality
- [ ] Code follows project style guidelines
- [ ] Documentation updated
- [ ] No console errors or warnings
- [ ] Services start without errors
- [ ] Health checks return UP
- [ ] Frontend builds successfully
- [ ] Linting passes

## Review Process

1. **Automated Checks**: CI/CD pipeline runs tests
2. **Code Review**: Maintainers review code
3. **Feedback**: Address review comments
4. **Approval**: PR approved by maintainer
5. **Merge**: PR merged to main branch

## Getting Help

- **Issues**: Ask questions in GitHub Issues
- **Discussions**: Use GitHub Discussions for general questions
- **Documentation**: Check `/docs` directory

## Recognition

Contributors will be acknowledged in:
- README contributors section
- Release notes
- Project documentation

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

**Thank you for contributing to CloudWatchr!** 🚀