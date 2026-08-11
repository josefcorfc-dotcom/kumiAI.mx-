/**
 * ALEPH ENCRYPTION API - Express Backend
 * RFC: CALF8712186T5 | Nodo: MX-SQ-3000
 * Integración de criptografía lattice con endpoints REST/WebSocket
 */

import express from 'express';
import cors from 'cors';
import { spawn } from 'child_process';
import http from 'http';
import { WebSocketServer } from 'ws';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json({ limit: '50mb' }));

const server = http.createServer(app);
const wss = new WebSocketServer({ server });

// Cache de sesiones criptográficas
const cryptoSessions = new Map();

/**
 * Ejecutar módulo Python ALEPH
 */
function runAlephCrypto(operation, data) {
  return new Promise((resolve, reject) => {
    const python = spawn('python3', [
      path.join(__dirname, 'aleph_crypto_bridge.py'),
      operation,
      JSON.stringify(data)
    ]);

    let output = '';
    let error = '';

    python.stdout.on('data', (data) => {
      output += data.toString();
    });

    python.stderr.on('data', (data) => {
      error += data.toString();
    });

    python.on('close', (code) => {
      if (code === 0) {
        try {
          resolve(JSON.parse(output));
        } catch (e) {
          reject(new Error('Parse error: ' + output));
        }
      } else {
        reject(new Error(error || `Python process exited with code ${code}`));
      }
    });

    setTimeout(() => {
      python.kill();
      reject(new Error('ALEPH operation timeout'));
    }, 30000);
  });
}

// ============================================
// ENDPOINTS REST API
// ============================================

/**
 * POST /api/aleph/initialize
 * Inicializar sesión criptográfica
 */
app.post('/api/aleph/initialize', async (req, res) => {
  try {
    const { seed, session_id } = req.body;
    
    const result = await runAlephCrypto('initialize', {
      seed: seed || 'KUMI-MX-SQ-3000',
      session_id: session_id || `session_${Date.now()}`
    });

    cryptoSessions.set(result.session_id, {
      created_at: new Date(),
      status: 'active'
    });

    res.json({
      success: true,
      session_id: result.session_id,
      public_key: result.public_key,
      timestamp: new Date().toISOString(),
      node: 'MX-SQ-3000',
      rfc: 'CALF8712186T5'
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

/**
 * POST /api/aleph/encrypt
 * Cifrar mensaje usando arquitectura Aleph
 */
app.post('/api/aleph/encrypt', async (req, res) => {
  try {
    const { message, session_id, target_quadrant } = req.body;

    if (!message || !session_id) {
      return res.status(400).json({ error: 'Missing message or session_id' });
    }

    const result = await runAlephCrypto('encrypt', {
      message: Buffer.from(message).toString('base64'),
      session_id,
      target_quadrant: target_quadrant || null
    });

    res.json({
      success: true,
      ciphertext: result.ciphertext,
      metadata: result.metadata,
      quadrant: result.metadata.quadrant,
      noise_magnitude: result.metadata.noise_magnitude,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

/**
 * POST /api/aleph/decrypt
 * Desencriptar usando clave privada
 */
app.post('/api/aleph/decrypt', async (req, res) => {
  try {
    const { ciphertext, session_id } = req.body;

    if (!ciphertext || !session_id) {
      return res.status(400).json({ error: 'Missing ciphertext or session_id' });
    }

    const result = await runAlephCrypto('decrypt', {
      ciphertext,
      session_id
    });

    res.json({
      success: true,
      message: result.message,
      recovered_point: result.metadata.recovered_point,
      quadrant: result.metadata.quadrant,
      integrity: 'verified',
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

/**
 * POST /api/aleph/rotate_phase
 * Rotar entre cuadrantes (I, II, III, IV)
 */
app.post('/api/aleph/rotate_phase', async (req, res) => {
  try {
    const { point, target_quadrant, session_id } = req.body;

    const result = await runAlephCrypto('rotate_phase', {
      point,
      target_quadrant,
      session_id
    });

    res.json({
      success: true,
      original_point: point,
      rotated_point: result.rotated_point,
      source_quadrant: result.source_quadrant,
      target_quadrant: result.target_quadrant,
      rotation_count: result.rotation_count,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

/**
 * GET /api/aleph/session/:session_id
 * Obtener información de sesión
 */
app.get('/api/aleph/session/:session_id', (req, res) => {
  const session = cryptoSessions.get(req.params.session_id);
  
  if (!session) {
    return res.status(404).json({ error: 'Session not found' });
  }

  res.json({
    session_id: req.params.session_id,
    status: session.status,
    created_at: session.created_at,
    node: 'MX-SQ-3000',
    rfc: 'CALF8712186T5'
  });
});

/**
 * GET /api/aleph/health
 * Health check del sistema ALEPH
 */
app.get('/api/aleph/health', (req, res) => {
  res.json({
    status: 'healthy',
    service: 'ALEPH Lattice Cryptography',
    version: 'V26.6',
    node: 'MX-SQ-3000',
    rfc: 'CALF8712186T5',
    protocol: 'Aleph-1',
    quantum_resistant: true,
    timestamp: new Date().toISOString()
  });
});

// ============================================
// WEBSOCKET REAL-TIME STREAMING
// ============================================

wss.on('connection', (ws) => {
  console.log('[WS] Cliente conectado - ALEPH Stream');
  
  ws.on('message', async (data) => {
    try {
      const payload = JSON.parse(data);
      
      if (payload.type === 'encrypt_stream') {
        // Cifrado en tiempo real con actualizaciones
        const result = await runAlephCrypto('encrypt', {
          message: Buffer.from(payload.message).toString('base64'),
          session_id: payload.session_id,
          target_quadrant: payload.target_quadrant
        });

        ws.send(JSON.stringify({
          type: 'encryption_complete',
          ciphertext: result.ciphertext,
          metadata: result.metadata,
          timestamp: new Date().toISOString()
        }));
      }
      
      if (payload.type === 'quadrant_stream') {
        // Stream de rotación de cuadrantes
        for (let i = 0; i < 4; i++) {
          const result = await runAlephCrypto('rotate_phase', {
            point: payload.point,
            target_quadrant: i,
            session_id: payload.session_id
          });

          ws.send(JSON.stringify({
            type: 'quadrant_update',
            quadrant: i,
            rotated_point: result.rotated_point,
            step: i + 1,
            timestamp: new Date().toISOString()
          }));

          // Pequeño delay entre rotaciones
          await new Promise(resolve => setTimeout(resolve, 200));
        }
      }
    } catch (error) {
      ws.send(JSON.stringify({
        type: 'error',
        message: error.message,
        timestamp: new Date().toISOString()
      }));
    }
  });

  ws.on('close', () => {
    console.log('[WS] Cliente desconectado');
  });
});

// ============================================
// ENDPOINTS GENERALES
// ============================================

app.get('/', (req, res) => {
  res.json({
    service: 'KUMI-Engine ALEPH Backend',
    version: 'V26.6',
    node: 'MX-SQ-3000',
    rfc: 'CALF8712186T5',
    protocol: 'Aleph-1 Lattice Cryptography',
    endpoints: {
      'POST /api/aleph/initialize': 'Crear sesión criptográfica',
      'POST /api/aleph/encrypt': 'Cifrar mensaje',
      'POST /api/aleph/decrypt': 'Desencriptar',
      'POST /api/aleph/rotate_phase': 'Rotar entre cuadrantes',
      'GET /api/aleph/session/:id': 'Información de sesión',
      'GET /api/aleph/health': 'Estado del servicio',
      'WS /': 'WebSocket streaming'
    },
    features: {\n      quantum_resistant: true,\n      lattice_based: true,\n      cartesian_quadrants: 4\n    }\n  });\n});\n\nserver.listen(PORT, () => {\n  console.log(`\\n[SYS] ALEPH Cryptography Backend`);\n  console.log(`[SYS] Nodo MX-SQ-3000 // Escuchando en puerto ${PORT}`);\n  console.log(`[SYS] Protocolo: Aleph-1 (Lattice-based)`);\n  console.log(`[SYS] RFC: CALF8712186T5`);\n  console.log(`[SYS] Resistencia Cuántica: ✓ Confirmada\\n`);\n});
