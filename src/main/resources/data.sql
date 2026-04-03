-- Seed data: inserted after Hibernate creates the schema via ddl-auto=create-drop
-- createdAt uses CURRENT_TIMESTAMP so times reflect when the server started.

INSERT INTO app_user (first_name, last_name, email, note, created_at) VALUES
  ('Alice',   'Johnson',  'alice.johnson@example.com',  'Prefers async communication. Meeting notes in Confluence.', CURRENT_TIMESTAMP),
  ('Bob',     'Smith',    'bob.smith@example.com',      'Backend engineer. Primary contact for API design reviews.', CURRENT_TIMESTAMP),
  ('Carol',   'Williams', 'carol.williams@example.com', 'Product manager. Owns the roadmap and sprint planning.', CURRENT_TIMESTAMP),
  ('David',   'Brown',    'david.brown@example.com',    'On-call rotation lead. Escalation path for P0 incidents.', CURRENT_TIMESTAMP),
  ('Eve',     'Davis',    'eve.davis@example.com',      'Frontend specialist. Angular and RxJS subject-matter expert.', CURRENT_TIMESTAMP);
